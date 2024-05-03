import ChatHeaderMenu from "./ChatHeaderMenuComponent.tsx";
import {Box} from "@mui/material";
import {useRef, useState} from "react";
import TextField from "@mui/material/TextField";
import ChatEditingButton from "./ChatEditingButton.tsx";
import ChatHeaderModifyModal from "./ChatHeaderModifyModal.tsx";
import {useChatStorage} from "../../stores/chatStorage.tsx";

const ChatNameComponent = ({name}: { name: string }) => {
    const {sendMessage} = useChatStorage();
    const [isChatNameEditing, setIsChatNameEditing] = useState(false);
    const [isHeaderEditing, setIsHeaderEditing] = useState(false);
    const inputRef = useRef<HTMLInputElement>(null);
    const [inputValue, setInputValue] = useState<string>(name)
    const handleInputChange = (e: any) => {
        setInputValue(e.target.value);
    };

    const changeName = () => {
        const data = {
            type: 'CHAT_NAME',
            data: {
                name: inputValue
            }
        }
        sendMessage(JSON.stringify(data));
        setInputValue('');
        setIsChatNameEditing((prev) => !prev);
    }
    return (
        <>
            <Box
                display='flex'
                alignItems='center'
                gap={2}
            >
                {
                    !isChatNameEditing ?
                        <span>{name}</span> :
                        <TextField
                            ref={inputRef}
                            value={inputValue}
                            onChange={(e) => handleInputChange(e)}
                            label="채널명"/>
                }
                {
                    !isChatNameEditing ?
                        <ChatHeaderMenu setIsChatNameEditing={setIsChatNameEditing}
                                        setIsHeaderEditing={setIsHeaderEditing}/>
                        :
                        <ChatEditingButton update={changeName} setIsEditing={setIsChatNameEditing}/>
                }
                {
                    isHeaderEditing && <ChatHeaderModifyModal open={isHeaderEditing} setOpen={setIsHeaderEditing}/>
                }
            </Box>
        </>
    );
}

export default ChatNameComponent;