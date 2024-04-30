import Box from "@mui/material/Box";
import omessLogo from "../../assets/omess_logo.png";
import { styled } from "@mui/material";

const SignInLogoBox = styled(Box)({
    width: "100%",
    display: "flex",
    justifyContent: "center",
});

const SignInLogo = () => {
    return (
        <SignInLogoBox>
            <img src={omessLogo}></img>
        </SignInLogoBox>
    );
};

export default SignInLogo;
