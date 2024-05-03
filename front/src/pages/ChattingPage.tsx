import {useEffect} from "react";
import ChatHeaderComponent from "../components/chat/ChatHeaderComponent.tsx";
import ChatHistoryComponent from "../components/chat/ChatHistoryComponent.tsx";
import ChatEditorComponent from "../components/chat/ChatEditorComponent.tsx";
import {Alert, Box} from "@mui/material";
import {ChatInfo} from "../types/chat/chat.ts";
import {useChatStorage} from "../stores/chatStorage.tsx";


const ChattingPage = ({chat}: { chat: ChatInfo }) => {
    const {init, client} = useChatStorage();

    useEffect(() => {
        init(chat);
    }, [chat]);
    return (
        <Box
            display="flex"
            flexDirection="column"
            height="100%"
        >
            {client == null ? <Alert severity="error">서버와 연결상태를 확인해 주세요.</Alert> : null}
            {/* Header */}
            <Box flex="1">
                <ChatHeaderComponent {...chat} />
            </Box>

            {/* Body */}
            <ChatHistoryComponent/>

            {/* Footer */}
            <ChatEditorComponent/>
        </Box>
    );
}

export default ChattingPage;