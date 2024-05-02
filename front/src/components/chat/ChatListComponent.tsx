import AddIcon from '@mui/icons-material/Add';
import {Avatar, Box, Divider, IconButton, Link} from "@mui/material";
import {useEffect, useState} from "react";
import {ChatInfo} from "../../types/chat/chat.ts";
import ArrowRightIcon from "@mui/icons-material/ArrowRight";

const ChatListComponent = ({projectId}: { projectId: number }) => {
    const [chatList, setChatList] = useState<ChatInfo[] | null>(null);

    console.log(setChatList)
    useEffect(() => {
        (async () => {
            // const data = await loadChatList(projectId).then(d => d.map(r => r.body));
            // setChatList([...data]);
        })()
    }, [projectId]);

    return (
        <>
            {/* Header */}
            <Box
                display="flex"
                alignItems="center"
                justifyContent="space-between"
            >
                <Box
                    display="flex"
                    alignItems="center"
                >
                    <IconButton>
                        <ArrowRightIcon/>
                    </IconButton>
                    <span>채팅</span>
                </Box>
                <AddIcon></AddIcon>
            </Box>
            {/* Body */}
            {
                chatList ? (chatList.map(value => (
                    <Box
                        p={1}
                    >
                        <Link href=""
                              color="inherit"
                              underline="none"
                              variant="body1"
                              display="flex"
                              gap={1}
                        >
                            <Avatar
                                sx={{width: 20, height: 20, fontSize: "12px"}}
                                variant="rounded"
                            >{value.memberCount}</Avatar>
                            {value.name}</Link>
                    </Box>
                ))) : null
            }
            <Divider variant="middle"/>
        </>
    );
}

export default ChatListComponent;