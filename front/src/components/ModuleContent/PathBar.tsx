import { Typography } from "@mui/material";
import Box from "@mui/material/Box";
import styled from "@mui/system/styled";

const PathBarBox = styled(Box)({
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "#fef7ff",
    height: "32px",
});

const PathBar = () => {
    return (
        <PathBarBox>
            <Typography>제로 / 채팅 / 휘파람</Typography>
        </PathBarBox>
    );
};

export default PathBar;
