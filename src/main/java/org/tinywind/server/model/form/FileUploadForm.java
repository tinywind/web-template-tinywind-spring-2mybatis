package org.tinywind.server.model.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.tinywind.server.util.spring.BaseForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileUploadForm extends BaseForm {
    private String data;
    private String fileName;

    public boolean validate(String prefix, BindingResult bindingResult) {
        if (StringUtils.isEmpty(data)) reject(bindingResult, prefix + ".data", "validator.blank", prefix + ".data");
        if (StringUtils.isEmpty(fileName)) reject(bindingResult, prefix + ".fileName", "validator.blank", prefix + ".fileName");
        return validate(bindingResult);
    }
}
