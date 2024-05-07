import {commands} from "@uiw/react-md-editor";
import {styled} from "@mui/material";

export const customCommands = [
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


export const VisuallyHiddenInput = styled('input')({
    clipPath: 'inset(50%)',
    height: 1,
    overflow: 'hidden',
    position: 'absolute',
    bottom: 0,
    left: 0,
    whiteSpace: 'nowrap',
    width: 1,
});