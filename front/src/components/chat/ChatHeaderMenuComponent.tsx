import * as React from 'react';
import {useState} from 'react';
import IconButton from '@mui/material/IconButton';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import MoreVertIcon from '@mui/icons-material/MoreVert';


const options = [
    '채팅방 이름 변경',
    '헤더 변경'
];

type Props = {
    setIsChatNameEditing: any,
    setIsHeaderEditing: any,
}
const ChatHeaderMenu = (props: Props) => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const handleClick = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    }
    const handleEvent = (option:string) => {
        setAnchorEl(null);
        const idx = options.findIndex(value => value === option);
        if (idx === 0) {
            props.setIsChatNameEditing(true);
        } else if (idx === 1) {
            props.setIsHeaderEditing(true);
        }
    };

    return (
        <div>
            <IconButton
                aria-label="more"
                id="long-button"
                aria-controls={open ? 'long-menu' : undefined}
                aria-expanded={open ? 'true' : undefined}
                aria-haspopup="true"
                onClick={handleClick}
            >
                <MoreVertIcon/>
            </IconButton>
            <Menu
                id="long-menu"
                MenuListProps={{
                    'aria-labelledby': 'long-button',
                }}
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
            >
                {options.map((option) => (
                    <MenuItem key={option} onClick={() => handleEvent(option)}>
                        {option}
                    </MenuItem>
                ))}
            </Menu>
        </div>
    );
}

export default ChatHeaderMenu;