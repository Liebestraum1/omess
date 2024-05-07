import {Box} from "@mui/material";
import ChatRoomHeader from "./ChatRoomHeader.tsx";
import ChatRoomContent from "./ChatRoomContent.tsx";

const ChatRoomInfoComponent = ({setIsOpened, selectedTab, setSelectedTab}: {
    setIsOpened: any,
    selectedTab: string,
    setSelectedTab: any
}) => {
    return (
        <Box
            borderLeft={0.5}
            height='100%'
        >
            <ChatRoomHeader setIsOpened={setIsOpened} selectedTab={selectedTab} setSelectedTab={setSelectedTab}/>

            {/* Body */}
            <ChatRoomContent selectedTab={selectedTab}/>
        </Box>
    );
}

export default ChatRoomInfoComponent;