import {useEffect, useState} from "react";
import ChatHeaderComponent from "../components/chat/ChatHeaderComponent.tsx";
import ChatHistoryComponent from "../components/chat/ChatHistoryComponent.tsx";
import ChatEditorComponent from "../components/chat/ChatEditorComponent.tsx";
import {Alert, Box} from "@mui/material";
import {useChatStorage} from "../stores/chatStorage.tsx";
import ChatRoomInfoComponent from "../components/chat/ChatRoomInfoComponent.tsx";
import {useSignInStore} from "../stores/SignInStorage.tsx";
import {useNavigate, useParams} from "react-router-dom";


const ChattingPage = () => {
    const navigate = useNavigate();
    const {chatId} = useParams();
    const {enter, setChat, chatList, chatInfo, init, client} = useChatStorage();
    const [isOpened, setIsOpened] = useState(false);
    const [selectedTab, setSelectedTab] = useState('info');
    const {memberId} = useSignInStore();

    useEffect(() => {
        if (chatId == undefined || chatList == undefined || chatList.length < 1) {
            navigate("/");
        } else {
            const temp = chatList!.filter(value => value.id === chatId)[0]
            setChat(temp)
        }
    }, [chatId]);

    useEffect(() => {
        console.log(client)
        if (client !== null) {
            enter(memberId!);
        } else {
            init(memberId!);
        }
    }, [chatInfo]);

    return (
        chatInfo &&
        <Box display='flex'
             height="100%"
             width='100%'
        >
            <Box
                display="flex"
                width='100%'
                flexDirection="column"
            >
                {client == null ? <Alert severity="error">서버와 연결상태를 확인해 주세요.</Alert> : null}
                {/* Header */}
                <Box flex={1}>
                    <ChatHeaderComponent chat={chatInfo} setIsOpened={setIsOpened} setSelectedTab={setSelectedTab}/>
                </Box>

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