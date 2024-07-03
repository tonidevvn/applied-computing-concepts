import React from 'react';
import Image from 'next/image';
import MainLogo from '../../public/food2logo-white.svg';
import Link from "next/link";

function Logo() {
    return (
        <div
            className='demo-logo p-4 m-4 flex items-center justify-center'
            style={{
                marginRight: 16,
            }}
        >
            <Link href='/'>
            <Image
                src={MainLogo}
                height={60}
                width={80}
                alt="Food food logo" /></Link>
        </div>
    );
}

export default Logo;