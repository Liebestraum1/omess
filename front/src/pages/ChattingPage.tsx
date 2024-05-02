import {useEffect} from "react";
import ChatHeaderComponent from "../components/chat/ChatHeaderComponent.tsx";
import ChatHistoryComponent from "../components/chat/ChatHistoryComponent.tsx";
import ChatEditorComponent from "../components/chat/ChatEditorComponent.tsx";
import {Box} from "@mui/material";
import {ChatInfo} from "../types/chat/chat.ts";
import {useChatStorage} from "../stores/ChatStorage.tsx";

const ChattingPage = ({chat}: { chat: ChatInfo }) => {
    const {init, sendMessage} = useChatStorage();

    useEffect(() => {
        init(chat);
    }, [chat]);
    return (
        <Box>
            {/* header */}
            <ChatHeaderComponent {...chat}/>
            {/* body */}
            <ChatHistoryComponent/>
            {/* typing */}
            <ChatEditorComponent/>
        </Box>
    );
}

export default ChattingPage;