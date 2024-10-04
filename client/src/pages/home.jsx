import useAuth from "../context/useAuth";

function Home() {
  const { accessToken } = useAuth();
  console.log(accessToken);
  return <div>This Home page is visible to all</div>;
}

export default Home;
