import {Button} from "@mui/material";
import PersonIcon from "@mui/icons-material/Person";
import Box from "@mui/material/Box";

const MemberCountCompnent = ({count}: { count: number }) => {
    return (
        <Button>
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