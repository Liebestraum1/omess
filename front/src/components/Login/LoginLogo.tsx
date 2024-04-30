import Box from "@mui/material/Box";
import omessLogo from "../../assets/omess_logo.png";
import { styled } from "@mui/material";

const LoginLogoBox = styled(Box)({
    width: "100%",
    display: "flex",
    justifyContent: "center",
});

const LoginLogo = () => {
    return (
        <LoginLogoBox>
            <img src={omessLogo}></img>
        </LoginLogoBox>
    );
};

export default LoginLogo;
