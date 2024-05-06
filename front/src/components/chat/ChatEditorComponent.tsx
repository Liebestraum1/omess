import {Box, Button, styled} from "@mui/material";
import {useEffect, useState} from "react";
import {useChatStorage} from "../../stores/chatStorage.tsx";
import MDEditor, {commands} from '@uiw/react-md-editor';
import SendRoundedIcon from '@mui/icons-material/SendRounded';
import AttachFileRoundedIcon from '@mui/icons-material/AttachFileRounded';
import '../../styles/MdEditor.css'


const ChatEditorComponent = () => {
    const {sendMessage} = useChatStorage();

    const [value, setValue] = useState<string | undefined>(undefined);
    const [isActive, setIsActive] = useState<boolean>(false);

    useEffect(() => {
        if (value == null || undefined) return;
        if (value!.length >= 1) {
            setIsActive(true);
        } else {
            setIsActive(false);
        }
    }, [value]);

    const send = () => {
        if (!isActive) return;
        const data = {
            type: 'SEND',
            data: {
                message: value
            }
        }
        setValue('')
        sendMessage(JSON.stringify(data))
        setIsActive(false);
    }
    const handleKeyDown = (event: React.KeyboardEvent<HTMLDivElement>) => {
        if (event.key === 'Enter' && !event.shiftKey && !event.altKey && !event.ctrlKey && !event.metaKey) { //shift + enter, alt + enter?? x + enter 면 다됨 난 그냥 엔터만
            send();
            event.preventDefault();
        }
    }

    const customCommands = [
        commands.bold, // 굵게
        commands.italic, // 기울임꼴
        commands.strikethrough, // 취소선
        commands.title,
        commands.divider, // 구분선
        commands.link, // 링크
        commands.code, // 코드
        commands.quote, // 인용문
        commands.unorderedListCommand, // 번호 없는 목록
        commands.orderedListCommand, // 번호 있는 목록
    ];


    const VisuallyHiddenInput = styled('input')({
        clipPath: 'inset(50%)',
        height: 1,
        overflow: 'hidden',
        position: 'absolute',
        bottom: 0,
        left: 0,
        whiteSpace: 'nowrap',
        width: 1,
    });
    const sendBtn = {
        name: 'send',
        keyCommand: 'send',
        icon: (
            <Box display='flex'
                 alignItems='center'
                 p={1}
            >
                <Button
                    className='file-btn'
                    component="label"
                    role={undefined}
                    variant="contained"
                    tabIndex={-1}
                >
                    <AttachFileRoundedIcon/>
                    <VisuallyHiddenInput type="file"/>
                </Button>
                <Button
                    disabled={!isActive}
                    onClick={() => send()}
                    className={isActive ? 'send-btn active' : 'send-btn deactive'}
                >
                    <SendRoundedIcon className='send-icon'/>
                </Button>
            </Box>
        ),
    };
    return (
        <Box p={3}
        >
            <MDEditor data-color-mode="light"
                      value={value}
                      onChange={(v) => setValue(v)}
                      onKeyPress={handleKeyDown}
                      textareaProps={{
                          placeholder: '메시지 입력...',
                          maxLength: 16383
                      }}
                      height='100%'
                      visibleDragbar={false}
                      preview={"edit"}
                      toolbarBottom={true}
                      commands={customCommands}
                      extraCommands={[
                          sendBtn
                      ]}
            ></MDEditor>
        </Box>
    );
}

export default ChatEditorComponent