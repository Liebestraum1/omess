import { Box, styled } from "@mui/material";
import { useState } from "react";
import FormInput from "../Common/FormInput";

const SignInFormBox = styled(Box)({
    display: "flex",
    flexDirection: "column",
    component: "form",
});

const SignInForm = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [nickname, setNickname] = useState("");

    const signInFormSubmitFunction = () => {
        const loginFormData = new FormData();
        loginFormData.append("email", email);
        loginFormData.append("password", password);
        loginFormData.append("nickname", nickname);
    };

    const singInFormInputProps = [
        { type: "email", label: "EMAIL", helperText: "이메일을 입력하세요.", onFormData: setEmail },
        { type: "password", label: "PASSWORD", helperText: "비밀번호를 입력하세요.", onFormData: setPassword },
        { type: "nickname", label: "NICKNAME", helperText: "닉네임을 입력하세요.", onFormData: setNickname },
    ];

    return (
        <SignInFormBox
            onSubmit={(e) => {
                e.preventDefault();
                signInFormSubmitFunction();
            }}
        >
            {singInFormInputProps.map((signFormInputProp) => (
                <FormInput
                    key={signFormInputProp.label}
                    type={signFormInputProp.type}
                    label={signFormInputProp.label}
                    helperText={signFormInputProp.helperText}
                    onFormData={signFormInputProp.onFormData}
                ></FormInput>
            ))}
        </SignInFormBox>
    );
};

export default SignInForm;
