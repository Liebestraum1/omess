import {Box, Button, Divider, Tab, Tabs} from "@mui/material";
import CloseIcon from '@mui/icons-material/Close';
import React from "react";

const ChatRoomHeader = ({setIsOpened, selectedTab, setSelectedTab}: {
    setIsOpened: any,
    selectedTab: string,
    setSelectedTab: any
}) => {
    return (
        <Box>
            <Box
                display="flex"
                justifyContent="space-between"
                px={2}
            >
                <Box>
                    <Box
                        py={1.5}
                    >
                        <span>채팅방 정보</span>
                    </Box>
                    <Tabs
                        value={selectedTab}
                        onChange={(e, val) => setSelectedTab(val)}
                        textColor="secondary"
                        indicatorColor="secondary"
                    >
                        <Tab value="info" label="정보"/>
                        <Tab value="member" label="멤버"/>
                        <Tab value="pin" label="핀"/>
                    </Tabs>
                </Box>
                <Button
                    color='inherit'
                    onClick={() => setIsOpened(false)}
                >
                    <CloseIcon/>
                </Button>
            </Box>
            <Divider/>
        </Box>
    );
}

export default ChatRoomHeader;