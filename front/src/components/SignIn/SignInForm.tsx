import { Fragment, useEffect, useState } from "react";
import { Box, styled } from "@mui/material";
import { useNavigate } from "react-router";
import { useSignInStore } from "../../stores/SignInStorage";
import { SignInStatus } from "../../types/SignIn/SignIn";
import SignInFormSubmitButton from "./SignInFormSubmitButton";
import SignUpButton from "../SiginUp/SignUpButton";
import { SignInRequest, SignInResponse, signInApi } from "../../services/SignIn/SignInApi";
import FormInput, { FormInputProp } from "../Common/FormInput";
import SignSnackBar, { AlertProp } from "../Common/SignSnackbar";

const SignInFormBox = styled(Box)({
    display: "flex",
    flexDirection: "column",
    width: "33%",
});

const SignInForm = ({ signInStatus }: { signInStatus: SignInStatus }) => {
    const navigator = useNavigate();
    const { setServerSignIn, setMemberSignIn } = useSignInStore();

    const [signInFormInputProps, setSignInFormInputProps] = useState<FormInputProp[]>([]);

    const [serverUrl, setServerUrl] = useState<string>("");
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");

    const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
    const [alertProp, setAlertProp] = useState<AlertProp>({
        severity: undefined,
        title: "",
        content: "",
    });

    const signInFormHandler = (e: React.FormEvent) => {
        e.preventDefault();
        if (signInStatus == "none") {
            console.log(serverUrl);
            setServerSignIn();
        } else {
            const signInRequest: SignInRequest = {
                email: email,
                password: password,
            };

            signInApi(signInRequest)
                .then((signInResponse: SignInResponse) => {
                    setMemberSignIn(signInResponse.memberId, signInResponse.nickname);
                    navigator("/main");
                })
                .catch(() => {
                    setAlertProp({
                        severity: "error",
                        title: "로그인 실패!",
                        content: "로그인에 실패했습니다.",
                    });
                    setOpenSnackbar(true);
                });
        }
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
        } else {
            setSignInFormInputProps([
                { type: "email", label: "EMAIL", helperText: "이메일을 입력하세요.", onFormData: setEmail },
                { type: "password", label: "PASSWORD", helperText: "비밀번호를 입력하세요.", onFormData: setPassword },
            ]);
        }
    }, [signInStatus]);

    return (
        <Fragment>
            <SignInFormBox
                component={"form"}
                onSubmit={(e) => {
                    signInFormHandler(e);
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
            <SignSnackBar
                open={openSnackbar}
                alertProp={alertProp}
                onClose={() => setOpenSnackbar(false)}
            ></SignSnackBar>
        </Fragment>
    );
};

export default SignInForm;
