import {Button} from "@mui/material";
import PersonIcon from "@mui/icons-material/Person";
import Box from "@mui/material/Box";

const MemberCountCompnent = ({count, setIsOpened, setSelectedTab}: {
    count: number,
    setIsOpened: any,
    setSelectedTab: any
}) => {
    const handleSelectedTab = () => {
        setSelectedTab('member');
        setIsOpened(true);
    }
    return (
        <Button
            color='secondary'
            onClick={handleSelectedTab}
        >
            <Box
                display="flex"
                gap={2}
                alignItems="center">
                <PersonIcon></PersonIcon>
                <span>{count}</span>
            </Box>
        </Button>
    );
}

export default MemberCountCompnent;