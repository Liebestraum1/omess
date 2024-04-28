import { Box, styled } from "@mui/material";
import LoginButton from "./LoginButton";
import LoginFormInput from "./LoginFormInput";
import { LoginStatusProps, LoginFormInputProp } from "../../types/Login/LoginProps";
import { useEffect, useState } from "react";
import { useLoginStore } from "../../stores/LoginStorage";
import { useNavigate } from "react-router";

const LoginFormBox = styled(Box)({
    display: "flex",
    flexDirection: "column",
    width: "33%",
});

const LoginForm = ({ loginStatus }: { loginStatus: LoginStatusProps }) => {
    const navigator = useNavigate();
    const { setServerLogin, setUserLogin } = useLoginStore();

    const [loginFormInputProps, setLoginFormInputProps] = useState<LoginFormInputProp[]>([]);

    const [serverUrl, setServerUrl] = useState<string>("");
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [loginFormSubmitFuction, setLoginFormSubmitFuction] = useState<() => void>(() => {});

    const serverLoginFormHandler = () => {
        const loginFormData = new FormData();
        loginFormData.append("url", serverUrl);
        setServerLogin();
    };

    const userLoginFormHandler = () => {
        const loginFormData = new FormData();
        loginFormData.append("email", email);
        loginFormData.append("password", password);
        setUserLogin();
        navigator("/main");
    };

    useEffect(() => {
        if (loginStatus == "none") {
            setLoginFormInputProps([
                {
                    type: "text",
                    label: "SERVER URL / PORT",
                    helperText: "서버의 주소와 포트를 입력하세요.",
                    onFormData: setServerUrl,
                },
            ]);

            setLoginFormSubmitFuction(() => () => serverLoginFormHandler());
        } else {
            setLoginFormInputProps([
                { type: "email", label: "EMAIL", helperText: "이메일을 입력하세요.", onFormData: setEmail },
                { type: "password", label: "PASSWORD", helperText: "비밀번호를 입력하세요.", onFormData: setPassword },
            ]);

            setLoginFormSubmitFuction(() => () => userLoginFormHandler());
        }
    }, [loginStatus]);

    return (
        <LoginFormBox
            component={"form"}
            onSubmit={(e) => {
                e.preventDefault();
                loginFormSubmitFuction();
            }}
        >
            {loginFormInputProps.map((loginFormInputProp) => (
                <LoginFormInput
                    key={loginFormInputProp.label}
                    type={loginFormInputProp.type}
                    label={loginFormInputProp.label}
                    helperText={loginFormInputProp.helperText}
                    onFormData={loginFormInputProp.onFormData}
                ></LoginFormInput>
            ))}
            <LoginButton loginStatus={loginStatus}></LoginButton>
        </LoginFormBox>
    );
};

export default LoginForm;
