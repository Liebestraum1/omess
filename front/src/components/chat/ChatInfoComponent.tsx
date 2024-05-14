import {Button} from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";

const ChatInfoComponent = ({setIsOpened, setSelectedTab}: {
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
            color='secondary'
            onClick={handleSelectedTab}
        >
            <InfoIcon/>
        </Button>
    )
}

export default ChatInfoComponent;