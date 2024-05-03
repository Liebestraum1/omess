import {FormControl, InputLabel, MenuItem, Select, SelectChangeEvent, Typography} from "@mui/material";
import Box from "@mui/material/Box";
import {useState} from "react";

type IssueCreateStatusFilterProp = {
    onChangeStatus: (status: number | null) => void;
}

const IssueCreateStatusFilter = ({onChangeStatus}: IssueCreateStatusFilterProp) => {

    const [selectedImpotance, setSelectedImpotanceLocal] = useState('');
    const handleChange = (event: SelectChangeEvent<string>) => {
        setSelectedImpotanceLocal(event.target.value);

        if (event.target.value == "") {
            onChangeStatus(null);
        } else {
            onChangeStatus(parseInt(event.target.value));
        }
    };

    return (
        <Box>
            {/** 중요도 필터 */}
            <FormControl sx={{m: 1, minWidth: 120}} size={"small"}>
                <InputLabel id="demo-simple-select-label" sx={{paddingTop: 0.5}}>진행상태</InputLabel>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={selectedImpotance}
                    label="중요도"
                    onChange={handleChange}
                    sx={{height: 49, paddingTop: 1.1}}
                >
                    <MenuItem value="">
                        <Typography> 선택해 주세요 </Typography>
                    </MenuItem>
                    <MenuItem value={1}>
                        <Typography> 진행전 </Typography>
                    </MenuItem>
                    <MenuItem value={2}>
                        <Typography> 진행중 </Typography>
                    </MenuItem>
                    <MenuItem value={3}>
                        <Typography> 완료 </Typography>
                    </MenuItem>
                </Select>
            </FormControl>
        </Box>
    );
}

export default IssueCreateStatusFilter;