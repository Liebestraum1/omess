import {useEffect, useState} from "react";
import ChatHeaderComponent from "../components/chat/ChatHeaderComponent.tsx";
import ChatHistoryComponent from "../components/chat/ChatHistoryComponent.tsx";
import ChatEditorComponent from "../components/chat/ChatEditorComponent.tsx";
import {Alert, Box} from "@mui/material";
import {ChatInfo} from "../types/chat/chat.ts";
import {useChatStorage} from "../stores/chatStorage.tsx";
import ChatRoomInfoComponent from "../components/chat/ChatRoomInfoComponent.tsx";


const ChattingPage = ({chat}: { chat: ChatInfo }) => {
    const {init, client} = useChatStorage();
    const [isOpened, setIsOpened] = useState(false);
    const [selectedTab, setSelectedTab] = useState('info');

    useEffect(() => {
        init(chat);
    }, [chat]);
    return (
        <Box display='flex'
             height="100%"
        >
            <Box
                display="flex"
                width='100%'
                flexDirection="column"
            >
                {client == null ? <Alert severity="error">서버와 연결상태를 확인해 주세요.</Alert> : null}
                {/* Header */}
                <ChatHeaderComponent chat={chat} setIsOpened={setIsOpened} setSelectedTab={setSelectedTab}/>

                {/* Body */}
                <ChatHistoryComponent/>

                {/* Footer */}
                <ChatEditorComponent/>
            </Box>
            {
                isOpened ?
                    <Box
                        width='75%'
                    >
                        <ChatRoomInfoComponent setIsOpened={setIsOpened}
                                               setSelectedTab={setSelectedTab}
                                               selectedTab={selectedTab}/>
                    </Box> : null
            }
        </Box>
    );
}

export default ChattingPage;