const {test} = require('node:test');
const assert = require('node:assert/strict');
const {readFileSync} = require('node:fs');
const {JSDOM} = require('jsdom');
const jquery = require('jquery');

function page() {
    const dom = new JSDOM('<!doctype html><body></body>', {runScripts: 'outside-only'});
    const $ = jquery(dom.window);
    dom.window.jQuery = $;
    dom.window.eval(readFileSync('src/main/webapp/resources/js/jquery.ex.js', 'utf8'));
    return {dom, $};
}

for (const field of ['__proto__.polluted', 'constructor.prototype.polluted', 'user[__proto__].polluted', 'user[constructor].prototype.polluted', 'user.!__proto__[label].value']) {
    test('form serialization skips unsafe names: ' + field, async () => {
        const {dom, $} = page();
        try {
            const form = $('<form>').append($('<input>', {name: field, value: 'value'}));
            const result = await Promise.resolve(form.asJsonData());
            assert.equal(result.polluted, undefined);
            assert.equal(dom.window.Object.prototype.polluted, undefined);
        } finally { dom.window.close(); }
    });
}

test('form serialization preserves nested maps and repeated fields', async () => {
    const {dom, $} = page();
    try {
        const form = $('<form>').append(
            $('<input>', {name: 'user.name', value: 'Alice'}),
            $('<input>', {name: 'labels[en]', value: 'Name'}),
            $('<input>', {name: 'tags', value: 'a', multiple: 'multiple'}),
            $('<input>', {name: 'tags', value: 'b', multiple: 'multiple'})
        );
        assert.equal(JSON.stringify(await Promise.resolve(form.asJsonData())),
            JSON.stringify({user: {name: 'Alice'}, labels: {en: 'Name'}, tags: ['a', 'b']}));
    } finally { dom.window.close(); }
});

for (const path of ['user.toString.label', 'user[toString].label', 'user.name', 'user[name].label']) {
    test('form serialization skips scalar traversal in ' + path, async () => {
        const {dom, $} = page();
        const builtins = [dom.window.Object.prototype, dom.window.String.prototype,
            dom.window.String.prototype.toString, dom.window.Array.prototype];
        const properties = builtins.map(value => Object.getOwnPropertyNames(value));
        try {
            const form = $('<form>').append(
                $('<input>', {name: 'user', value: 'Alice'}),
                $('<input>', {name: path, value: 'label'}),
                $('<input>', {name: 'title', value: 'Report'})
            );
            const result = await Promise.resolve(form.asJsonData());
            assert.equal(JSON.stringify(result), JSON.stringify({user: 'Alice', title: 'Report'}));
            for (let i = 0; i < builtins.length; i++) {
                assert.deepEqual(Object.getOwnPropertyNames(builtins[i]), properties[i]);
            }
        } finally { dom.window.close(); }
    });
}

test('form serialization creates own arrays for repeated inherited property names', async () => {
    const {dom, $} = page();
    try {
        const form = $('<form>').append(
            $('<input>', {name: 'toString', value: 'a', multiple: 'multiple'}),
            $('<input>', {name: 'toString', value: 'b', multiple: 'multiple'}),
            $('<input>', {name: 'labels[toString]', value: 'c', multiple: 'multiple'}),
            $('<input>', {name: 'labels[toString]', value: 'd', multiple: 'multiple'})
        );
        assert.equal(JSON.stringify(await Promise.resolve(form.asJsonData())),
            JSON.stringify({toString: ['a', 'b'], labels: {toString: ['c', 'd']}}));
    } finally { dom.window.close(); }
});

test('form serialization skips repeated fields that conflict with a scalar', async () => {
    const {dom, $} = page();
    try {
        const form = $('<form>').append(
            $('<input>', {name: 'tags', value: 'first'}),
            $('<input>', {name: 'tags', value: 'second', multiple: 'multiple'})
        );
        assert.equal(JSON.stringify(await Promise.resolve(form.asJsonData())), JSON.stringify({tags: 'first'}));
    } finally { dom.window.close(); }
});
