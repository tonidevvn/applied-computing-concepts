'use client'
import React, { useEffect, useState } from 'react'
import Link from 'next/link'
import { FaBars, FaTimes } from 'react-icons/fa'
import { Flex, Popover } from 'antd'

export default function Navbar() {
    const [nav, setNav] = useState(false)
    const links = [
        {
            id: 1,
            link: '/',
            title: 'Home',
        },
        // {
        //     id: 12,
        //     link: 'keyword-search',
        //     title: 'Keyword Search',
        // },
    ]
    // Function to hide nav on resize
    const handleResize = () => {
        if (window.innerWidth >= 768) {
            // Assuming 768px is your md breakpoint
            setNav(false)
        }
    }

    // Set up event listener for window resize
    useEffect(() => {
        window.addEventListener('resize', handleResize)

        // Clean up the event listener
        return () => {
            window.removeEventListener('resize', handleResize)
        }
    }, [])

    function loadSubmenus(
        submenus: { id: number; link: string; title: string }[]
    ) {
        return (
            <Flex vertical={true}>
                {submenus.map(({ id, link, title }) => (
                    <Link href={link}>{title}</Link>
                ))}
            </Flex>
        )
    }

    return (
        <>
            <ul className='hidden md:flex'>
                {links.map(({ id, link, title, submenus }) => (
                    <li
                        key={id}
                        className='nav-links px-4 cursor-pointer capitalize text-xl text-gray-500 hover:text-white duration-200 link-underline'
                    >
                        {!!submenus ? (
                            <Popover
                                placement='bottom'
                                content={loadSubmenus(submenus)}
                            >
                                <Link href='#'>{title}</Link>
                            </Popover>
                        ) : (
                            <Link href={link}>{title}</Link>
                        )}
                    </li>
                ))}
            </ul>

            <div
                onClick={() => setNav(!nav)}
                className='cursor-pointer pr-4 z-20 text-gray-500 md:hidden absolute top-2 right-2'
            >
                {nav ? <FaTimes size={40} /> : <FaBars size={40} />}
            </div>

            {nav && (
                <ul className='flex flex-col justify-center items-center absolute top-0 left-0 w-full h-screen bg-gradient-to-b from-black to-gray-800 text-gray-500 z-10'>
                    {links.map(({ id, link, title }) => (
                        <li
                            key={id}
                            className='px-4 cursor-pointer capitalize py-6 text-4xl'
                        >
                            <Link onClick={() => setNav(!nav)} href={link}>
                                {title}
                            </Link>
                        </li>
                    ))}
                </ul>
            )}
        </>
    )
}
