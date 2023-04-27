import React, { useEffect, useState, createContext } from "react";
import { Button, Spin, Statistic } from "antd";
import CartItem from "./CartItem";
import useCheckout from "./hooks/useCheckout";
import useClearCart from "./hooks/useClearCart";
import useShowOrder from "./hooks/useShowOrder";


const TotalPriceContext = createContext(null);

// This component is used to show the cart.
function Cart() {
  const [totalPrice, setTotalPrice] = useState({});
  const [successPurchase, setSuccessPurchase] = useState(false);
  const { data, isLoading, isRefetching, refetch, remove } = useShowOrder();
  const { mutate: checkout } = useCheckout(() => {
    refetch();
    setSuccessPurchase(true);
  });
  const { mutate: clearCart } = useClearCart(() => {
    refetch();
  });

  useEffect(() => {
    remove();
    refetch();
  }, []);

  if (isLoading) {
    return <Spin />;
  }
  if (successPurchase) {
    return <div>Purchased Successfully!</div>;
  }

  if (!data || !data?.Data?.length) {
    return <div>Empty cart</div>;
  }


  return (
  <TotalPriceContext.Provider value={totalPrice}>
    <Spin spinning={isRefetching}>
      <div style={{ padding: 12 }}>
          {data?.Data?.map((item) => (
            <CartItem id={item.pid} quantity={item.product_count} setTotalPrice={setTotalPrice}/>
          ))}
        <div style={{ padding: 12 }}>
        <Statistic title="Total Price" value={Object.keys(totalPrice).map((k) => totalPrice[k] || 0).reduce((partialSum, a) => partialSum + a, 0)} />
        </div>
        {Object.keys(data).length > 0 && (
          <Button onClick={clearCart} type="primary" danger>
            Clear cart
          </Button>
        )}
        {Object.keys(data).length > 0 && (
          <Button onClick={checkout} type="primary" style={{ marginLeft: 16 }}>
            Checkout
          </Button>
        )}
      </div>
    </Spin>
    </TotalPriceContext.Provider>
  );
}

export default Cart;
