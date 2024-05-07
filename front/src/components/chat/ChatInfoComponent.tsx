import {Button, IconButton} from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";

const ChatInfoComponent = ({chatId, setIsOpened, setSelectedTab}: {
    chatId: string,
    setIsOpened: any,
    setSelectedTab: any
}) => {
    const handleSelectedTab = () => {
        setIsOpened(true);
        setSelectedTab('info')
    }
    return (
        <Button
            onClick={handleSelectedTab}
        >
            <IconButton aria-label="info" size="small">
                <InfoIcon/>
            </IconButton>
        </Button>
    )
}

export default ChatInfoComponent;