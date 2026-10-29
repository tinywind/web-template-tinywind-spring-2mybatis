# Security behavior

Stored files must have a single file name. Uploads and downloads reject absolute paths, separators, dot traversal, and control characters. Downloads stay within `application.file.location`, reject symbolic links, and close their input stream. Uploads create a new file exclusively without following links. Failure logs omit supplied names and paths.

Unsafe HTTP requests require a session-bound CSRF token. First load `/`, retain the session cookie, and read the `_csrf` and `_csrf_header` meta tags. Send the token in the named header on POST, PUT, PATCH, and DELETE requests. The existing jQuery helpers add this header only for unsafe requests to the same origin. A missing token, an invalid token, or a token from another session returns 403. After logout, load a new page and token before another unsafe request.

Logout uses `POST /api/user/logout`. Login continues to accept JSON through `POST /api/user/login`. The two transaction demonstration routes use POST and require login. GET requests no longer change this state. API clients, including Swagger UI try-outs, must supply a session CSRF token for unsafe requests.

The `/ws/` SockJS HTTP transports use Spring's same-origin checks instead of the servlet CSRF token. Native WebSocket and SockJS handshakes reject a foreign Origin. Other paths, including names beginning with `ws-`, remain subject to CSRF protection.

Email validation uses an expression without backtracking across repeated segments. Hardware addresses come from the JDK network interface API instead of PATH-resolved commands. JSON form serialization blocks prototype keys and creates objects without inherited properties. Modal attributes select existing DOM elements and are never parsed as HTML.

The full Maven verification passes 19 tests. The focused regressions use real temporary files, a symbolic link, actual network interfaces, adversarial email inputs, and servlet CSRF processing. The exact packaged WAR passes 43 HTTP checks, a native WebSocket upgrade and foreign-origin rejection, real file/repository round trips with database rollback, and isolated Chrome checks for form serialization, modal safety, and same-origin token headers. The filtered Maven metadata is loaded for the JSP asset version so Tomcat can serve the local scripts. These checks do not cover a full authenticated user workflow or production deployment.
