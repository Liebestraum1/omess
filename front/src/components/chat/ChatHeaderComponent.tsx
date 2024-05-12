import {Box, Divider} from "@mui/material";
import {ChatInfo} from "../../types/chat/chat.ts";
import ChatHeaderDataComponent from "./ChatHeaderDataComponent.tsx";
import MemberCountComponent from "./MemberCountComponent.tsx";
import ChatNameComponent from "./ChatNameComponent.tsx";
import ChatInfoComponent from "./ChatInfoComponent.tsx";
import PinComponent from "./PinComponent.tsx";

const ChatHeaderComponent = ({chat, setIsOpened, setSelectedTab}: {
    chat: ChatInfo,
    setIsOpened: any,
    setSelectedTab: any
}) => {
    return (
        <Box>
            <Box
                display="flex"
                justifyContent="space-between"
                px={2}
                py={1}
            >
                <Box>
                    <Box
                        display="flex"
                        flexDirection="column"
                    >
                        <Box>
                            <ChatNameComponent name={chat.name}></ChatNameComponent>
                        </Box>
                        <Box
                            display="flex"
                            gap={2}
                            alignItems="center"
                        >
                            <MemberCountComponent count={chat.memberCount} setIsOpened={setIsOpened} setSelectedTab={setSelectedTab}/>
                            <PinComponent pinCount={chat.pinCount} setIsOpened={setIsOpened} setSelectedTab={setSelectedTab}/>
                            <ChatHeaderDataComponent header={chat.header}/>
                        </Box>
                    </Box>
                </Box>
                <ChatInfoComponent chatId={chat.id} setIsOpened={setIsOpened} setSelectedTab={setSelectedTab}/>
            </Box>
            <Divider/>
        </Box>
    );
}

export default ChatHeaderComponent;