import { useState } from "react";
import avatar from "../assets/avatar.svg";
import useAuth from "../context/useAuth";
import genericPostService from "../service/genericPostService";

function Navbar() {
  const [showMenu, setShowMenu] = useState(false);
  const { userEmail, logout } = useAuth();

  const handleLogout = async () => {
    const response = await genericPostService("auth/logout", "");

    console.log("logout response: ");
    console.log(response);

    if (response.status === 200) {
      logout();
    }
  };

  return (
    <>
      <nav className="w-screen h-14 p-4 bg-yellow-400 flex justify-between items-center">
        <p className="text-3xl">LOGO</p>
        <p className="text-3xl">NAVBAR</p>
        <div>
          <button className="relative" onClick={() => setShowMenu(true)}>
            <img
              src={avatar}
              alt="user avatar icon"
              className="w-[40px] h-[40px]"
            />
          </button>

          {/* // ! user menu */}
          {showMenu && (
            <>
              <div
                className="top-0 left-0 right-0 bottom-0 z-[999] bg-slate-200 fixed bg-opacity-30"
                onClick={() => setShowMenu((prev) => !prev)}
              ></div>

              <div className="absolute right-4 h-[200px] w-[200px] bg-slate-200 z-[1000]">
                <div className="w-full h-full flex flex-col gap-4 items-center justify-center">
                  <img
                    src={avatar}
                    alt="user avatar big"
                    className="h-[60px] w-[60px]"
                  />

                  <p>{userEmail}</p>

                  <button onClick={handleLogout}>Logout</button>
                </div>
              </div>
            </>
          )}
        </div>
      </nav>
    </>
  );
}

export default Navbar;
