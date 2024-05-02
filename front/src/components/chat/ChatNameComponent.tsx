import {IconButton} from "@mui/material";
import {KeyboardArrowDown} from "@mui/icons-material";

const ChatNameComponent = ({name}: { name: string }) => {
    return (
        <>
            <span>{name}</span>
            <IconButton aria-label="menu" size="small">
                <KeyboardArrowDown/>
            </IconButton>
        </>
    );
}

export default ChatNameComponent;