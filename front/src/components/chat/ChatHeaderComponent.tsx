import {Box, Divider} from "@mui/material";
import {ChatInfo} from "../../types/chat/chat.ts";
import ChatHeaderDataComponent from "./ChatHeaderDataComponent.tsx";
import MemberCountComponent from "./MemberCountComponent.tsx";
import ChatNameComponent from "./ChatNameComponent.tsx";
import ChatInfoComponent from "./ChatInfoComponent.tsx";

const ChatHeaderComponent = (chat: ChatInfo) => {
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
                            gap={4}
                            alignItems="center"
                        >
                            <MemberCountComponent count={4}/>
                            <ChatHeaderDataComponent header={chat.header}/>
                        </Box>
                    </Box>
                </Box>
                <ChatInfoComponent chatId={chat.id}/>
            </Box>
            <Divider/>
        </Box>
    );
}

export default ChatHeaderComponent;