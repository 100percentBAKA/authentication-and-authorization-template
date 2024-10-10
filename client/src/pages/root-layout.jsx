import { Outlet } from "react-router-dom";

function RootLayout() {
  return (
    <>
      <div className="text-4xl text-center">These are the testing pages</div>
      <Outlet />
    </>
  );
}

export default RootLayout;
