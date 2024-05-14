import {Box, Button} from "@mui/material";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import PushPinIcon from '@mui/icons-material/PushPin';
import PushPinOutlinedIcon from '@mui/icons-material/PushPinOutlined';

type Props = {
    setIsEditing: (value: any) => void;
    delete: () => void;
    isPined: boolean;
    pin: () => void;
    writer: number;
    memberId: number;
}
const ChatMessageMenu = (props: Props) => {

    return (
        <Box
            boxShadow={3}
            borderRadius={3}
            gap={2}
            sx={{padding: '5px'}}
        >
            <Button color="secondary" aria-label="pin"
                    size='small'
                    onClick={() => props.pin()}
                    sx={{
                        '&:hover': {backgroundColor: 'indigo'}
                    }}
            >
                {props.isPined ? <PushPinIcon/> : <PushPinOutlinedIcon/>}
            </Button>
            {props.writer === props.memberId &&
                <Box>
                    <Button color="secondary" aria-label="edit"
                            size='small'
                            onClick={() => props.setIsEditing(true)}
                            sx={{
                                '&:hover': {backgroundColor: 'indigo'}
                            }}
                    >
                        <EditIcon/>
                    </Button>
                    <Button color="secondary" aria-label="delete"
                            size='small'
                            onClick={() => props.delete()}
                            sx={{
                                '&:hover': {backgroundColor: 'indigo'}
                            }}
                    >
                        <DeleteIcon/>
                    </Button>
                </Box>
            }
        </Box>
    );
}

export default ChatMessageMenu;