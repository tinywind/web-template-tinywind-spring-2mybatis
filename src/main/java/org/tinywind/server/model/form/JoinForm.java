package org.tinywind.server.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.tinywind.server.util.spring.BaseForm;
import org.tinywind.server.util.valid.NotNull;

import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class JoinForm extends BaseForm {
    @ApiModelProperty(value = "아이디", required = true)
    @NotNull("아이디")
    private String loginId;
    @ApiModelProperty(value = "비밀번호", required = true)
    @NotNull("비밀번호")
    private String password;
    @ApiModelProperty(value = "비밀번호 확인", required = true)
    @NotNull("비밀번호 확인")
    private String passwordConfirm;
    @NotNull("프로필 이미지")
    private FileUploadForm profileImage;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (!Objects.equals(password, passwordConfirm))
            reject(bindingResult, "password",  "validator.equal", "비밀번호", "비밀번호 확인");

        if (profileImage != null)
            profileImage.validate("profileImage.", bindingResult);

        return super.validate(bindingResult);
    }
}
