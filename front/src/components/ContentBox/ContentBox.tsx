import Box from "@mui/material/Box";
import styled from "@mui/system/styled";

const ContentBoxComponent = styled(Box)({
    flex: 7,
    backgroundColor: ".main",
});

const ContentBox = () => {
    return <ContentBoxComponent></ContentBoxComponent>;
};

export default ContentBox;
