'use client'
import React, {useState} from 'react';
import Link from "next/link";
import { FaBars, FaTimes } from "react-icons/fa";

export default function Navbar() {
    const [nav, setNav] = useState(false)
    const links = [
        {
            id: 1,
            link: "/",
            title: "Home",
        },
        {
            id: 2,
            link: "about",
            title: "About",
        },
        {
            id: 3,
            link: "shop",
            title: "Shop",
        },
        {
            id: 4,
            link: "contact",
            title: "Contact",
        },
    ];

    return (
        <>
            <ul className="hidden md:flex">
                {links.map(({ id, link, title }) => (
                    <li
                        key={id}
                        className="nav-links px-4 cursor-pointer capitalize text-xl text-gray-500 hover:scale-105 hover:text-white duration-200 link-underline"
                    >
                        <Link href={link}>{title}</Link>
                    </li>
                ))}
            </ul>

            <div
                onClick={() => setNav(!nav)}
                className="cursor-pointer pr-4 z-20 text-gray-500 md:hidden absolute top-2 right-2"
            >
                {nav ? <FaTimes size={40} /> : <FaBars size={40} />}
            </div>

            {nav && (
                <ul className="flex flex-col justify-center items-center absolute top-0 left-0 w-full h-screen bg-gradient-to-b from-black to-gray-800 text-gray-500 z-10">
                    {links.map(({ id, link, title }) => (
                        <li
                            key={id}
                            className="px-4 cursor-pointer capitalize py-6 text-4xl"
                        >
                            <Link onClick={() => setNav(!nav)} href={link}>
                                {title}
                            </Link>
                        </li>
                    ))}
                </ul>
            )}</>
    )
}