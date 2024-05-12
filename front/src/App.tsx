import SignInPage from "./pages/SignInPage";
import MainPage from "./pages/MainPage";
import { Routes, Route, BrowserRouter } from "react-router-dom";

const App = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<SignInPage />} />
                <Route path="/main/*" element={<MainPage />} />
            </Routes>
        </BrowserRouter>
    );
};

export default App;
