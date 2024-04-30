import { Fragment, useEffect, useState } from "react";
import { Box, styled } from "@mui/material";
import { useNavigate } from "react-router";
import { useLoginStore } from "../../stores/LoginStorage";
import { FormInputProp } from "../../types/common/FormProps";
import { LoginStatusProps } from "../../types/Login/LoginProps";
import LoginFormSubmitButton from "./LoginFormSubmitButton";
import SignInButton from "../SiginIn/SignInButton";
import FormInput from "../Common/FormInput";

const LoginFormBox = styled(Box)({
    display: "flex",
    flexDirection: "column",
    width: "33%",
});

const LoginForm = ({ loginStatus }: { loginStatus: LoginStatusProps }) => {
    const navigator = useNavigate();
    const { setServerLogin, setUserLogin } = useLoginStore();

    const [loginFormInputProps, setLoginFormInputProps] = useState<FormInputProp[]>([]);

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
        <Fragment>
            <LoginFormBox
                component={"form"}
                onSubmit={(e) => {
                    e.preventDefault();
                    loginFormSubmitFuction();
                }}
            >
                {loginFormInputProps.map((loginFormInputProp) => (
                    <FormInput
                        key={loginFormInputProp.label}
                        type={loginFormInputProp.type}
                        label={loginFormInputProp.label}
                        helperText={loginFormInputProp.helperText}
                        onFormData={loginFormInputProp.onFormData}
                    ></FormInput>
                ))}
                <LoginFormSubmitButton key="login" loginStatus={loginStatus}></LoginFormSubmitButton>
            </LoginFormBox>
            {loginStatus !== "none" ? (
                <Box width={"33%"}>
                    <SignInButton />
                </Box>
            ) : null}
        </Fragment>
    );
};

export default LoginForm;
