import Box from "@mui/material/Box"
import Fab from '@mui/material/Fab';
import EditIcon from '@mui/icons-material/Edit'

export default function Display() {
    return (
            <Box sx={{ display: 'flex', height: '100vh' }}>
                <Box display="flex" alignItems={"center"} flexDirection="column" sx={{ width: 64, bgcolor: '#4F378B' }}>
                    <Fab size="medium" aria-label="edit" sx={{borderRadius: '12px'}}>
                        <EditIcon />
                    </Fab> 
                    <Fab size="medium" aria-label="edit" sx={{borderRadius: '12px'}}>
                        <EditIcon />
                    </Fab> 
                </Box>
                <Box sx={{ width: 200, bgcolor: '#E8DEF8' }}></Box>
                <Box sx={{ flex: 7, bgcolor: '.main' }}></Box>
            </Box>
    )
}