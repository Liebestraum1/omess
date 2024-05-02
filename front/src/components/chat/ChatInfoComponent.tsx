import {Button, IconButton} from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";

const ChatInfoComponent = ({chatId}: { chatId: string }) => {
    console.log(chatId);
    return (
        <Button
        >
            <IconButton aria-label="info" size="small">
                <InfoIcon/>
            </IconButton>
        </Button>
    )
}

export default ChatInfoComponent;