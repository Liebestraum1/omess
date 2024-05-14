import {Box, Typography} from "@mui/material";
import {useChatStorage} from "../../stores/chatStorage.tsx";
import {useEffect} from "react";

const ChatRoomContent = ({selectedTab}: { selectedTab: string }) => {
    const {sendMessage, chatInfo, members, pinMessages} = useChatStorage();

    const requestMembers = () => {
        if (members != null && members.length > 0) return;
        const request = {
            type: 'MEMBERS',
            data: null
        }
        sendMessage(JSON.stringify(request))
    }

    const requestPins = () => {
        const request = {
            type: 'LOAD_PIN',
            data: {
                offset: 0
            }
        }
        sendMessage(JSON.stringify(request))
    }

    useEffect(() => {
        switch (selectedTab) {
            case 'pin':
                requestPins()
                break;
            case 'member':
                requestMembers()
                break;
        }
    }, [selectedTab]);

    switch (selectedTab) {
        case 'info':
            return (
                <Box p={2}>
                    <Typography>채팅명: {chatInfo?.name}</Typography>
                    <Typography>공지 : {chatInfo?.header}</Typography>
                    <Typography>멤버 수 : {chatInfo?.memberCount}</Typography>
                </Box>
            );
        case 'member':
            return (
                <Box p={2}>
                    {
                        members?.map((value, index) => (
                            <Box key={index} display='flex' gap={3}>
                                <Typography>{value.nickname}</Typography>
                                <Typography>{value.role}</Typography>
                            </Box>
                        ))
                    }
                </Box>
            )
                ;
        case 'pin':
            return <Box>여기는 핀 탭 내용입니다.</Box>;
        default:
            return null;
    }
}

export default ChatRoomContent;