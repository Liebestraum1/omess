import {Box} from "@mui/material";
import {useChatStorage} from "../../stores/chatStorage.tsx";
import {useEffect} from "react";

const ChatRoomContent = ({selectedTab}: { selectedTab: string }) => {
    const {sendMessage, members, pinMessages} = useChatStorage();

    const requestMembers = () => {
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
                    <p>채팅방 이름</p>
                    <p>채팅방 주인</p>
                    <p>멤버 수</p>
                </Box>
            );
        case 'member':
            return <Box>여기는 멤버 탭 내용입니다.</Box>;
        case 'pin':
            return <Box>여기는 핀 탭 내용입니다.</Box>;
        default:
            return null;
    }
}

export default ChatRoomContent;