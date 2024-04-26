import { Box, styled } from "@mui/material";
import LoginButton from "./LoginButton";
import LoginFormInput from "./LoginFormInput";
import { LoginStatusProps, LoginFormInputProp } from "../../types/Login/LoginProps";
import { useEffect, useState } from "react";

const LoginFormBox = styled(Box)({
    display: "flex",
    flexDirection: "column",
    width: "33%",
});

const LoginForm = ({ loginStatus }: { loginStatus: LoginStatusProps }) => {
    const [loginFormInputProps, setLoginFormInputProps] = useState<LoginFormInputProp[]>([]);

    useEffect(() => {
        if (loginStatus == "none") {
            setLoginFormInputProps([{ label: "SERVER URL / PORT", helperText: "서버의 주소와 포트를 입력하세요." }]);
        } else {
            setLoginFormInputProps([
                { label: "EMAIL", helperText: "이메일을 입력하세요." },
                { label: "PASSWORLD", helperText: "비밀번호를 입력하세요." },
            ]);
        }
    }, [loginStatus]);

    return (
        <LoginFormBox>
            {loginFormInputProps.map((loginFormInputProp, index) => (
                <LoginFormInput
                    key={index}
                    label={loginFormInputProp.label}
                    helperText={loginFormInputProp.helperText}
                ></LoginFormInput>
            ))}
            <LoginButton loginStatus={loginStatus}></LoginButton>
        </LoginFormBox>
    );
};

export default LoginForm;
