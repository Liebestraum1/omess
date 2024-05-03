import { Box } from "@mui/material";
import { styled } from "@mui/system";
import ProfileButton from "./ProfileButton";

const NavBarBox = styled(Box)({
    height: "100%",
    backgroundColor: "#4F378B",
});

const NavBar = () => {
    return (
        <NavBarBox display={"flex"} justifyContent={"flex-end"} alignItems={"center"}>
            <ProfileButton></ProfileButton>
        </NavBarBox>
    );
};

export default NavBar;
