import Box from "@mui/material/Box";
import LoginLogo from "./LoginLogo";
import LoginTitle from "./LoginTitle";
import LoginForm from "./LoginForm";
import { useLoginStore } from "../../stores/LoginStorage";
import { styled } from "@mui/material";

const LoginBox = styled(Box)({
    width: "100%",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
});

const Login = () => {
    const { loginStatus } = useLoginStore();
    return (
        <LoginBox>
            <LoginLogo />
            <LoginTitle loginStatus={loginStatus}></LoginTitle>
            <LoginForm loginStatus={loginStatus}></LoginForm>
        </LoginBox>
    );
};

export default Login;
