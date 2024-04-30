import { Box, Typography, styled } from "@mui/material";
import { SignInStatus } from "../../types/SignIn/SignIn";
import { useEffect, useState } from "react";

const SignInTitleBox = styled(Box)({
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
});

const SignInTitleTypography = styled(Typography)({
    fontSize: 48,
    fontWeight: "medium",
});

const SignInTextTypography = styled(Typography)({});

const SignInTitle = ({ signInStatus }: { signInStatus: SignInStatus }) => {
    const [title, setTitle] = useState<string>("");
    const [text, setText] = useState<string>("");

    useEffect(() => {
        if (signInStatus == "none") {
            setTitle("서버 입장");
            setText("서버의 URL과 포트를 입력하세요.");
        } else {
            setTitle("로그인");
            setText("이메일과 비밀번호를 입력하세요.");
        }
    }, [signInStatus]);

    return (
        <SignInTitleBox>
            <SignInTitleTypography>{title}</SignInTitleTypography>
            <SignInTextTypography>{text}</SignInTextTypography>
        </SignInTitleBox>
    );
};

export default SignInTitle;
