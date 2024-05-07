import {FormControl, InputLabel, MenuItem, Rating, Select, SelectChangeEvent, Typography} from "@mui/material";
import Box from "@mui/material/Box";
import {useState} from "react";
import {useKanbanBoardStore} from "../../stores/KanbanBoardStorage.tsx";

const ImpotanceFilter = () => {
    const {setSelectedImpotance, kanbanBoardId} = useKanbanBoardStore();
    const [selectedImpotance, setSelectedImpotanceLocal] = useState('');
    const handleChange = (event: SelectChangeEvent<string>) => {
        setSelectedImpotanceLocal(event.target.value);
        if (event.target.value == "" && kanbanBoardId) {
            setSelectedImpotance(null, 1, kanbanBoardId);
        } else if(kanbanBoardId){
            setSelectedImpotance(parseInt(event.target.value), 1, kanbanBoardId);
        }
    };
    return (
        <Box>
            {/** 중요도 필터 */}
            <FormControl sx={{m: 1, minWidth: 120}} size={"small"}>
                <InputLabel id="demo-simple-select-label" sx={{paddingTop: 0.5}}>중요도</InputLabel>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={selectedImpotance}
                    label="중요도"
                    onChange={handleChange}
                    sx={{height: 49, paddingTop: 1.1}}
                >
                    <MenuItem value="">
                        <Box display={"flex"} justifyContent="space-between" alignItems="center" width={90} height={34}>
                            <Typography> 선택해 주세요 </Typography>
                        </Box>
                    </MenuItem>
                    <MenuItem value={3}>
                        <Box display={"flex"} justifyContent="space-between" alignItems="center" width={90}>
                            <Rating style={{paddingBottom: 10}} readOnly name="read-only" value={3} max={3}/>
                        </Box>
                    </MenuItem>
                    <MenuItem value={2}>
                        <Box display={"flex"} justifyContent="space-between" alignItems="center" width={90}>
                            <Rating style={{paddingBottom: 10}} readOnly name="read-only" value={2} max={3}/>
                        </Box>
                    </MenuItem>
                    <MenuItem value={1}>
                        <Box display={"flex"} justifyContent="space-between" alignItems="center" width={90}>
                            <Rating style={{paddingBottom: 10}} readOnly name="read-only" value={1} max={3}/>
                        </Box>
                    </MenuItem>
                </Select>
            </FormControl>
        </Box>
    );
}

export default ImpotanceFilter;