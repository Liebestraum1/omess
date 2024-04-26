import { Box, Typography, styled } from "@mui/material";
import { LoginStatusProps } from "../../types/Login/LoginProps";
import { useEffect, useState } from "react";

const LoginTitleBox = styled(Box)({
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
});

const LoginTitleTypography = styled(Typography)({
    fontSize: 48,
    fontWeight: "medium",
});

const LoginTextTypography = styled(Typography)({});

const LoginTitle = ({ loginStatus }: { loginStatus: LoginStatusProps }) => {
    const [title, setTitle] = useState<string>("");
    const [text, setText] = useState<string>("");

    useEffect(() => {
        if (loginStatus == "none") {
            setTitle("서버 입장");
            setText("서버의 URL과 포트를 입력하세요.");
        } else {
            setTitle("로그인");
            setText("이메일과 비밀번호를 입력하세요.");
        }
    }, [loginStatus]);

    return (
        <LoginTitleBox>
            <LoginTitleTypography>{title}</LoginTitleTypography>
            <LoginTextTypography>{text}</LoginTextTypography>
        </LoginTitleBox>
    );
};

export default LoginTitle;
