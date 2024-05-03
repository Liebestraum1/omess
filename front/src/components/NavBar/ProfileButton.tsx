import { Avatar, Fade, Menu, MenuItem } from "@mui/material";
import { useSignInStore } from "../../stores/SignInStorage";
import { Fragment } from "react/jsx-runtime";
import { useState } from "react";

const ProfileButton = () => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const { setUserLogout, memberNickname } = useSignInStore();
    console.log(memberNickname);

    const open = Boolean(anchorEl);
    const handleClick = (e: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(e.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    return (
        <Fragment>
            <Avatar
                onClick={(e) => handleClick(e)}
                sx={{
                    cursor: "pointer",
                    width: "32px",
                    height: "32px",
                    marginInline: "4px",
                }}
                alt={memberNickname}
                src="/static/images/avatar/1.jpg" // TODO => 차후 유저 이미지 받아오기
            />
            <Menu
                id="fade-menu"
                MenuListProps={{
                    "aria-labelledby": "fade-button",
                }}
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                TransitionComponent={Fade}
                autoFocus={false}
            >
                <MenuItem>My account</MenuItem>
                <MenuItem
                    onClick={() => {
                        handleClose();
                        setUserLogout();
                    }}
                >
                    Logout
                </MenuItem>
            </Menu>
        </Fragment>
    );
};

export default ProfileButton;
