package org.tinywind.server.model.form;

import org.tinywind.server.util.spring.BaseForm;
import org.tinywind.server.util.valid.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm extends BaseForm {
    @Schema(description = "아이디", required = true)
    @NotNull("아이디")
    private String id;
    @Schema(description = "비밀번호", required = true)
    @NotNull("비밀번호")
    private String password;
}
