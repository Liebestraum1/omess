import { Fragment, useEffect, useState } from "react";
import { Box, styled } from "@mui/material";
import { useNavigate } from "react-router";
import { useSignInStore } from "../../stores/SignInStorage";
import { FormInputProp } from "../../types/common/FormProps";
import { SignInStatus } from "../../types/SignIn/SignIn";
import SignInFormSubmitButton from "./SignInFormSubmitButton";
import SignUpButton from "../SiginUp/SignUpButton";
import FormInput from "../Common/FormInput";

const SignInFormBox = styled(Box)({
    display: "flex",
    flexDirection: "column",
    width: "33%",
});

const SignInForm = ({ signInStatus }: { signInStatus: SignInStatus }) => {
    const navigator = useNavigate();
    const { setServerSignIn, setUserSignIn } = useSignInStore();

    const [signInFormInputProps, setSignInFormInputProps] = useState<FormInputProp[]>([]);

    const [serverUrl, setServerUrl] = useState<string>("");
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [SignInFormSubmitFuction, setSignInFormSubmitFuction] = useState<() => void>(() => {});

    const serverSignInFormHandler = () => {
        const signInFormData = new FormData();
        signInFormData.append("url", serverUrl);
        setServerSignIn();
    };

    const userSignInFormHandler = () => {
        const signInFormData = new FormData();
        signInFormData.append("email", email);
        signInFormData.append("password", password);
        setUserSignIn();
        navigator("/main");
    };

    useEffect(() => {
        if (signInStatus == "none") {
            setSignInFormInputProps([
                {
                    type: "text",
                    label: "SERVER URL / PORT",
                    helperText: "서버의 주소와 포트를 입력하세요.",
                    onFormData: setServerUrl,
                },
            ]);

            setSignInFormSubmitFuction(() => () => serverSignInFormHandler());
        } else {
            setSignInFormInputProps([
                { type: "email", label: "EMAIL", helperText: "이메일을 입력하세요.", onFormData: setEmail },
                { type: "password", label: "PASSWORD", helperText: "비밀번호를 입력하세요.", onFormData: setPassword },
            ]);

            setSignInFormSubmitFuction(() => () => userSignInFormHandler());
        }
    }, [signInStatus]);

    return (
        <Fragment>
            <SignInFormBox
                component={"form"}
                onSubmit={(e) => {
                    e.preventDefault();
                    SignInFormSubmitFuction();
                }}
            >
                {signInFormInputProps.map((signInFormInputProp) => (
                    <FormInput
                        key={signInFormInputProp.label}
                        type={signInFormInputProp.type}
                        label={signInFormInputProp.label}
                        helperText={signInFormInputProp.helperText}
                        onFormData={signInFormInputProp.onFormData}
                    ></FormInput>
                ))}
                <SignInFormSubmitButton key="SignIn" signInStatus={signInStatus}></SignInFormSubmitButton>
            </SignInFormBox>
            {signInStatus !== "none" ? (
                <Box width={"33%"}>
                    <SignUpButton />
                </Box>
            ) : null}
        </Fragment>
    );
};

export default SignInForm;
