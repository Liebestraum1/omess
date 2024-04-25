import AddIcon from '@mui/icons-material/Add';
import {Avatar, Box, Divider} from "@mui/material";
import {useEffect, useState} from "react";
import {loadChatList} from "../../services/chat/ChatApiRequest.ts";
import {ChatInfo} from "../../types/chat/chat.ts";

const ChatListComponent = ({projectId}: { projectId: number }) => {
    const [chatList, setChatList] = useState<ChatInfo[] | null>(null);

    useEffect(() => {
        (async () => {
            const data = await loadChatList(projectId).then(d => d.map(r => r.body));
            setChatList([...data]);
        })()
    }, [projectId]);

    return (
        <>
            {/* Header */}
            <Box
                display="flex"
                justifyContent="space-between"
                p={5}
            >
                <span>채팅</span>
                <Divider/>
                <AddIcon></AddIcon>
            </Box>
            {/* Body */}
            {
                chatList ? (chatList.map(value => (
                    <Box
                        display="flex"
                        justifyContent="space-between"
                        p={3}
                    >
                        <Avatar>{value.memberCount}</Avatar>
                        <a href="">{value.name}</a>
                    </Box>
                ))) : null
            }
        </>
    );
}

export default ChatListComponent;