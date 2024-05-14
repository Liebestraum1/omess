import {Accordion, AccordionDetails, AccordionSummary, Avatar, Typography} from "@mui/material";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";
import Box from "@mui/material/Box";
import AddIcon from "@mui/icons-material/Add";
import ChatCreationModal from "./ChatCreationModal.tsx";
import {useChatStorage} from "../../stores/chatStorage.tsx";
import {useEffect, useState} from "react";
import {getChatList, leaveChat} from "../../services/chat/ChatApi.ts";
import CloseOutlinedIcon from '@mui/icons-material/CloseOutlined';
import {Link, useNavigate} from 'react-router-dom';

export const ChatAccordion = ({projectId}: { projectId: number | undefined }) => {
    const {chatList, setChatList, removeChat, chatId, reset} = useChatStorage();

    useEffect(() => {
        if (projectId) {
            getChatList(projectId).then((response) => {
                const {data} = response;
                setChatList(data);
            });
        }
    }, [projectId]);

    const [openChatCreationModal, setOpenChatCreationModal] = useState<boolean>(false);

    const navigator = useNavigate();
    const handleChatLeave = (chat: string) => {
        if (projectId == undefined) return;
        leaveChat(projectId, chat).then(() => {
            removeChat(chat);
            if (chatId === chat) {
                reset();
            }
            navigator('/main')
        }).catch((e) => console.error(e))
    }

    return (
        <>
            {
                projectId &&
                <>
                    <Accordion
                        sx={{boxShadow: 'none'}}
                    >
                        <AccordionSummary
                            expandIcon={<ArrowDropDownIcon/>}
                            aria-controls="panel2-content"
                            id="panel2-header"
                            sx={{
                                flexDirection: 'row-reverse',
                                backgroundColor: '#dbc6f7'
                            }}
                        >
                            <Box
                                display='flex'
                                justifyContent='space-between'
                                width='100%'
                            >
                                <Typography>채팅</Typography>
                                <AddIcon sx={{cursor: "pointer"}} onClick={(event) => {
                                    event.stopPropagation();
                                    setOpenChatCreationModal(true)
                                }}></AddIcon>
                            </Box>
                        </AccordionSummary>
                        <AccordionDetails
                            sx={{
                                padding: 0,
                                backgroundColor: '#cdbce3',
                            }}
                        >
                            {
                                chatList?.map((chat, key) => (
                                    <Link
                                        to={`/main/chat/${chat.id}`}
                                        key={key}
                                        style={{
                                            textDecoration: 'none',
                                            cursor: 'pointer',
                                            color: 'black'
                                        }}
                                    >
                                        <Box display='flex'
                                             justifyContent='space-between'
                                             maxHeight='2em'
                                             px={1}
                                             py={0.5}
                                             alignItems='center'
                                             sx={{
                                                 '&:hover': {
                                                     backgroundColor: '#b4a5c7',
                                                     '& .closeIcon': {
                                                         opacity: 1
                                                     }
                                                 },
                                             }}
                                        >
                                            <Avatar sx={{
                                                width: 24,
                                                height: 24,
                                                fontSize: '1em',
                                            }} variant="square">
                                                {chat.memberCount}
                                            </Avatar>
                                            <Typography sx={{
                                                overflow: 'hidden',
                                                textOverflow: 'ellipsis',
                                                whiteSpace: 'nowrap',
                                            }}>{chat.name}</Typography>
                                            <CloseOutlinedIcon
                                                onClick={() => handleChatLeave(chat.id)}
                                                className="closeIcon"
                                                sx={{
                                                    width: 20,
                                                    height: 20,
                                                    color: 'red',
                                                    opacity: 0
                                                }}
                                            />
                                        </Box>
                                    </Link>
                                ))
                            }
                        </AccordionDetails>
                    </Accordion>
                    <ChatCreationModal open={openChatCreationModal} setOpen={setOpenChatCreationModal}
                                       projectId={projectId}></ChatCreationModal>
                </>
            }
        </>
    );
}