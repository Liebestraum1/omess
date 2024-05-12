import ChattingPage from "./ChattingPage.tsx";
import ModuleContent from "../components/ModuleContent/ModuleContent.tsx";
import {Route, Routes} from "react-router-dom";

export const SubPage = () => {
    return (
        <Routes>
            <Route path="/module" element={<ModuleContent/>}/>
            <Route path="/chat/:chatId" element={<ChattingPage/>}/>
        </Routes>
    );
}