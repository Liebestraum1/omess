import { Box } from "@mui/material";
import { styled } from "@mui/system";

const NavBarBox = styled(Box)({
    height: "100%",
    backgroundColor: "#4F378B",
});

const NavBar = () => {
    return <NavBarBox></NavBarBox>;
};

export default NavBar;
